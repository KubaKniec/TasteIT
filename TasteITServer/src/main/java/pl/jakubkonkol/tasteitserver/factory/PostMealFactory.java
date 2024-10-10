package pl.jakubkonkol.tasteitserver.factory;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import pl.jakubkonkol.tasteitserver.model.*;
import pl.jakubkonkol.tasteitserver.model.enums.PostType;

@Component
public class PostMealFactory extends PostFactory {

    @Override
    public Post createPost(JSONObject postObj) {
        Post newPost = new Post();
        newPost.setPostType(PostType.FOOD);

        PostMedia postMedia = createPostMedia(postObj, "strMeal", "strMealThumb", "Very nice meal, drop like I will give u head");
        newPost.setPostMedia(postMedia);

        Recipe recipe = createRecipe(postObj, "strInstructions");
        recipe.setIngredientsWithMeasurements(createIngredients(postObj));
        newPost.setRecipe(recipe);

        // TODO: Set proper user and tags, likes, comments, etc.
        newPost.setUserId("213742069");
        newPost.setTags(null);
        newPost.setLikes(null);
        newPost.setComments(null);

        return newPost;
    }
}
