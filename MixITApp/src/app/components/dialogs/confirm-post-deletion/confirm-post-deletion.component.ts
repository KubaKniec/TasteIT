import { Component } from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {ConfirmDialogComponent} from "../confirm-dialog/confirm-dialog.component";

@Component({
  selector: 'app-confirm-post-deletion',
  templateUrl: './confirm-post-deletion.component.html',
  styleUrls: ['./confirm-post-deletion.component.css']
})
export class ConfirmPostDeletionComponent {

  constructor(public dialogRef: MatDialogRef<ConfirmDialogComponent>) {
  }
  onConfirm(): void {
    this.dialogRef.close(true);
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }
}
