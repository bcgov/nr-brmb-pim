import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'cirras-remove-planting-dialog',
  templateUrl: './remove-planting-dialog.component.html',
  styleUrls: ['./remove-planting-dialog.component.scss']
})
export class RemovePlantingDialogComponent implements OnInit {

  policyNumber: string 
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
