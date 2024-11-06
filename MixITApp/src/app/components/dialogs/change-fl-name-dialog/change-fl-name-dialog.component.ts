import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-change-fl-name-dialog',
  templateUrl: './change-fl-name-dialog.component.html',
  styleUrls: ['./change-fl-name-dialog.component.css']
})
export class ChangeFlNameDialogComponent {
  newName: string = '';

  constructor(
    public dialogRef: MatDialogRef<ChangeFlNameDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { currentName: string }
  ) {
    this.newName = data.currentName;
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onConfirm(): void {
    this.dialogRef.close(this.newName);
  }
}
