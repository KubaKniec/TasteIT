import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {debounceTime, distinctUntilChanged} from 'rxjs/operators';
import {Tag} from "../../../model/user/Tag";
import {EPostType} from "../../../model/post/EPostType";
import {TagType} from "../../../model/user/TagType";

@Component({
  selector: 'app-post-details',
  templateUrl: './post-details.component.html',
  styleUrls: ['./post-details.component.css']
})
export class PostDetailsComponent implements OnInit {
  postForm!: FormGroup;
  tagSearchControl = new FormControl('');
  availableTags: Tag[] = [];
  filteredTags: Tag[] = [];
  selectedTags: Tag[] = [];
  postTypes = ['Food', 'Drink']
  constructor(private fb: FormBuilder) {}

  ngOnInit() {
    this.postForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required, Validators.minLength(10)]],
      category: [null, Validators.required]
    });

    //placeholder
    this.availableTags = [
      { tagId: '1', tagName: 'Travel', tagType: TagType.DETAILED },
      { tagId: '2', tagName: 'Food', tagType: TagType.DETAILED },
      { tagId: '3', tagName: 'Lifestyle', tagType: TagType.DETAILED },
      { tagId: '4', tagName: 'Photography', tagType: TagType.DETAILED },
      { tagId: '5', tagName: 'Adventure', tagType: TagType.DETAILED }
    ];

    this.tagSearchControl.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged()
      )
      .subscribe(value => this.filterTags(value as string));
  }

  filterTags(searchTerm: string) {
    if (!searchTerm) {
      this.filteredTags = [];
      return;
    }

    this.filteredTags = this.availableTags
      .filter(tag =>
        tag.tagName.toLowerCase().includes(searchTerm.toLowerCase()) &&
        !this.selectedTags.some(selected => selected.tagId === tag.tagId)
      )
      .slice(0, 5);
  }

  addTag(tag: Tag) {
    if (!this.selectedTags.some(t => t.tagId === tag.tagId)) {
      this.selectedTags.push(tag);
      this.tagSearchControl.setValue('');
      this.filteredTags = [];
    }
  }

  removeTag(tag: Tag) {
    this.selectedTags = this.selectedTags.filter(t => t.tagId !== tag.tagId);
  }

  onTagSearch(event: Event) {
    const input = event.target as HTMLInputElement;
    this.filterTags(input.value);
  }

  onSubmit() {
    if (this.postForm.valid) {
      const formData = {
        ...this.postForm.value,
        tags: this.selectedTags
      };
      console.log(formData);
    }
  }
}
