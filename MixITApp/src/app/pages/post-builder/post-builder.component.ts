import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {PostData} from "./shared/postData";
import {EPostType} from "../../model/post/EPostType";
import {StorageUploadService} from "../../service/storage-upload.service";

type StepNumber = 1 | 2 | 3 | 4 | 5;
type StepsStatus = Record<StepNumber, boolean>;
@Component({
  selector: 'app-post-builder',
  templateUrl: './post-builder.component.html',
  styleUrls: ['./post-builder.component.css']
})
export class PostBuilderComponent implements OnInit{
  postData: PostData = {
    postMedia: {
      title: '',
      description: '',
      pictures: [],
    },
    recipe: {
      steps: new Map<number, string>(),
      pictures: [],
      ingredientsWithMeasurements: []
    },
    postType: EPostType.FOOD,
    tags: [],
  }
  picUrl: string = '';
  currentStep: StepNumber = 1;
  stepsStatus: StepsStatus = {
    1: false,
    2: false,
    3: false,
    4: false,
    5: false
  };
  constructor(
    private router: Router,
    private storageUploadService: StorageUploadService,
  ) {}

  async ngOnInit(): Promise<void> {

  }
  onStepStatusChange(step: StepNumber, status: boolean): void {
    this.stepsStatus[step] = status;
  }

  canProceed(): boolean {
    return this.stepsStatus[this.currentStep];
  }

  nextStep(): void {
    if (this.currentStep < 5) {
      this.currentStep = (this.currentStep + 1) as StepNumber;
    }
  }

  prevStep(): void {
    if (this.currentStep > 1) {
      this.currentStep = (this.currentStep - 1) as StepNumber;
    }
  }
  onUpdatePhoto(pictures: string[]){
    this.postData.postMedia.pictures = pictures;
    this.picUrl = pictures[0];
    // this.nextStep();
  }
  onUpdateDetails(postMedia: PostData['postMedia'], tags: PostData['tags']){
    this.postData.postMedia = postMedia;
    this.postData.tags = tags;
    this.nextStep();
  }
  onUpdateRecipe(recipe: PostData['recipe']){
    this.postData.recipe = recipe;
    this.nextStep();
  }
  onUpdateIngredients(ingredients: PostData['recipe']['ingredientsWithMeasurements']){
    this.postData.recipe.ingredientsWithMeasurements = ingredients;
    this.nextStep();
  }
  onClose(){
    this.storageUploadService.deleteFile(this.postData.postMedia.pictures[0])
    this.router.navigate(['/']);
  }
  async onSubmit(){
    console.log(this.postData);
  }

}
