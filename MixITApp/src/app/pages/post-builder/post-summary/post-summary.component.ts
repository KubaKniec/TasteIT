import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {PostBuilderModule} from "../shared/PostBuilderModule";
import {PostData} from "../shared/postData";

@Component({
  selector: 'app-post-summary',
  templateUrl: './post-summary.component.html',
  styleUrls: ['./post-summary.component.css']
})
export class PostSummaryComponent implements PostBuilderModule, OnInit{
  @Output() close: EventEmitter<void> = new EventEmitter<void>();
  @Output() nextStep: EventEmitter<any> = new EventEmitter<any>();
  @Output() prevStep: EventEmitter<void> = new EventEmitter<void>();
  @Input() postData!: PostData
  canProceed: boolean = false;


  ngOnInit(): void {
  }

  onClose(): void {
    this.close.emit();
  }
  onContinue() {
    this.nextStep.emit();
  }
  onPrevStep() {
    this.prevStep.emit();
  }

}
