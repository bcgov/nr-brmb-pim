import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogContent, MatDialogActions } from '@angular/material/dialog';
import { DIALOG_TYPE } from 'src/app/components/dialogs/base-dialog/base-dialog.component';
import { BaseDialogComponent } from '../../../dialogs/base-dialog/base-dialog.component';
import { CdkScrollable } from '@angular/cdk/scrolling';
import { MatButton } from '@angular/material/button';

@Component({
    selector: 'cirras-remove-planting-dialog',
    templateUrl: './remove-planting-dialog.component.html',
    styleUrls: ['./remove-planting-dialog.component.scss'],
    imports: [BaseDialogComponent, CdkScrollable, MatDialogContent, MatDialogActions, MatButton]
})
export class RemovePlantingDialogComponent implements OnInit {

  policyNumber: string 

  titleLabel = "Remove Linked Plantings"
  dialogType = DIALOG_TYPE.INFO;

  constructor(
    public dialogRef: MatDialogRef<String>,
    @Inject(MAT_DIALOG_DATA) public data: string) {  

      if (data) {
        //capture the data that comes from the main page
        this.policyNumber = data;
      } 
    }

  ngOnInit(): void {
  }

  onDeletePlanting() {
    this.dialogRef.close({event:'DeletePlanting'});
  }

  onRemoveLink() {
    this.dialogRef.close({event:'KeepPlanting'});
  }
}
