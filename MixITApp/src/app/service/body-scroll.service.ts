import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class BodyScrollService{
  private scrollDisabled = false;


  disableScroll() {
    if (!this.scrollDisabled) {
      document.body.style.overflow = 'hidden';
      this.scrollDisabled = true;
    }
  }

  enableScroll() {
    if (this.scrollDisabled) {
      document.body.style.overflow = 'auto';
      this.scrollDisabled = false;
    }
  }
}
