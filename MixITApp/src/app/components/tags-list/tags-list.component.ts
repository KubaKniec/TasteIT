import {Component, Input, OnInit} from '@angular/core';
import {Tag} from "../../model/user/Tag";
import {Router} from "@angular/router";

@Component({
  selector: 'app-tags-list',
  templateUrl: './tags-list.component.html',
  styleUrls: ['./tags-list.component.css']
})
export class TagsListComponent{
  @Input() tags: Tag[] = [];
  constructor(private router: Router) { }

  goToTag(tag: Tag) {
    this.router.navigate(['/tag', tag.tagId], {queryParams: {tagName: tag.tagName}}).then();
  }
}
