# preference_analysis/analysis_service.py
from typing import List, Dict, Any
import numpy as np
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity


class PreferenceAnalysisService:
    def __init__(self):
        self.vectorizer = TfidfVectorizer(
            stop_words='english',
            max_features=1000,
            ngram_range=(1, 2)
        )
        self.action_weights = {
            'LIKE_POST': 1.0,
            'COMMENT_POST': 1.5,
            'ADD_TO_FOODLIST': 2.0
        }

    def analyze_user_preferences(self, user_data: Dict[str, Any], clusters_data: Dict[str, Any]) -> Dict[str, Any]:
        try:
            print("\n=== Starting Preference Analysis ===")
            print(f"Analyzing preferences for user: {user_data.get('userId')}")

            # Przetwórz dane użytkownika
            user_tags = self._process_user_tags(user_data.get('tags', []))
            print(f"Processed user tags: {user_tags}")

            user_actions = self._process_user_actions(user_data.get('actions', []))
            print(f"Processed user actions: {user_actions}")

            all_preferences = user_tags + user_actions
            print(f"Total preferences: {len(all_preferences)}")

            if not all_preferences:
                print("No preferences found, returning empty result")
                return self._create_empty_result(user_data.get('userId'))

            # Przygotuj dane do analizy
            preference_text = ' '.join(all_preferences)
            print(f"Preference text: {preference_text[:200]}...")  # pokazujemy tylko część

            cluster_texts = {}
            for cluster_id, info in clusters_data.items():
                cluster_text = ' '.join(info.get('main_topics', []) + list(info.get('keyword_weights', {}).keys()))
                cluster_texts[cluster_id] = cluster_text
                print(f"\nCluster {cluster_id}:")
                print(f"- Name: {info.get('name')}")
                print(f"- Text: {cluster_text[:100]}...")  # pokazujemy tylko część

            if not cluster_texts:
                print("No cluster texts found, returning empty result")
                return self._create_empty_result(user_data.get('userId'))

            # Oblicz podobieństwa
            matrix = self.vectorizer.fit_transform([preference_text] + list(cluster_texts.values()))
            similarity_scores = cosine_similarity(matrix[0:1], matrix[1:])[0]
            print("\nSimilarity scores:")
            for idx, score in enumerate(similarity_scores):
                print(f"Cluster {list(clusters_data.keys())[idx]}: {score}")

            # Znajdź najlepsze dopasowania
            matched_clusters = []
            for idx, score in enumerate(similarity_scores):
                if score >= 0.01:  # próg podobieństwa
                    cluster_id = list(clusters_data.keys())[idx]
                    cluster_info = clusters_data[cluster_id]
                    matched_clusters.append({
                        'cluster_id': cluster_id,
                        'similarity_score': float(score),
                        'cluster_name': cluster_info.get('name', '')
                    })

            matched_clusters.sort(key=lambda x: x['similarity_score'], reverse=True)
            matched_clusters = matched_clusters[:3]  # top 3 klastrów

            print(f"\nFound {len(matched_clusters)} matching clusters:")
            for cluster in matched_clusters:
                print(
                    f"- Cluster {cluster['cluster_id']}: {cluster['cluster_name']} (score: {cluster['similarity_score']})")

            result = {
                'user_id': user_data.get('userId'),
                'matched_clusters': matched_clusters,
                'preference_score': float(
                    np.mean([c['similarity_score'] for c in matched_clusters])) if matched_clusters else 0.0
            }

            print("\n=== Analysis Complete ===")
            return result

        except Exception as e:
            print(f"\nError in preference analysis: {str(e)}")
            import traceback
            traceback.print_exc()
            return self._create_empty_result(user_data.get('userId'))

    def _process_user_tags(self, tags: List[Dict]) -> List[str]:
        print("\nProcessing post tags:")
        print(f"Input tags: {tags}")
        processed_tags = [tag.get('tagName', '').lower() for tag in tags if
                          isinstance(tag, dict) and tag.get('tagName')]
        print(f"Processed tags: {processed_tags}")
        return processed_tags

    def _process_user_actions(self, actions: List[Dict]) -> List[str]:
        print("\nProcessing user actions:")
        keywords = []
        for action in actions:
            print(f"\nProcessing action: {action.get('actionType')}")
            weight = self.action_weights.get(action.get('actionType'), 1.0)
            metadata = action.get('metadata', {})

            if action.get('actionType') == 'LIKE_POST' and metadata.get('post'):
                post = metadata['post']
                if post.get('postMedia', {}).get('title'):
                    words = [word.lower() for word in post['postMedia']['title'].split()]
                    print(f"Adding title words: {words} (weight: {weight})")
                    keywords.extend(words * int(weight))
                if post.get('tags'):
                    tags = self._process_user_tags(post['tags'])
                    print(f"Adding post tags: {tags}")
                    keywords.extend(tags)

            elif action.get('actionType') == 'COMMENT_POST' and metadata.get('commentContent'):
                words = [word.lower() for word in metadata['commentContent'].split()]
                print(f"Adding comment words: {words} (weight: {weight})")
                keywords.extend(words * int(weight))

        print(f"\nFinal keywords from actions: {keywords}")
        return keywords

    def _create_empty_result(self, user_id: str) -> Dict[str, Any]:
        return {
            'user_id': user_id,
            'matched_clusters': [],
            'preference_score': 0.0
        }
