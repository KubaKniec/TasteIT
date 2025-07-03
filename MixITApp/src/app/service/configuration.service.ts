import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class ConfigurationService {
  private _useRecommendationAlgorithm = true;

  setUseRecommendationAlgorithm(value: boolean): void {
    this._useRecommendationAlgorithm = value;
    localStorage.setItem('useRecommendationAlgorithm', JSON.stringify(value));
  }

  get useRecommendationAlgorithm(): boolean {
    const storedValue = localStorage.getItem('useRecommendationAlgorithm');
    return storedValue ? JSON.parse(storedValue) : true;
  }
}
