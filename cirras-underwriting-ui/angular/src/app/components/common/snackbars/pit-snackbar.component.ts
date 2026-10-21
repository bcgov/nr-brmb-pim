import {Component, Inject} from "@angular/core";
// External
import {MAT_SNACK_BAR_DATA, MatSnackBarRef} from "@angular/material/snack-bar";
import {PIT_SNACKBAR_TYPES, PitSnackbarData} from "../../../utils";
import { NgIf } from "@angular/common";
import { MatIcon } from "@angular/material/icon";

@Component({
    selector: 'cirras-underwriting-snackbar',
    template: `
      <div class="snack-bar-content">
        <div class="icon-panel">
          <mat-icon *ngIf="data.type == SNACKBAR_TYPES.SUCCESS">check_circle</mat-icon>
          <mat-icon *ngIf="data.type == SNACKBAR_TYPES.ERROR">cancel</mat-icon>
          <mat-icon *ngIf="data.type == SNACKBAR_TYPES.WARNING">info</mat-icon>
          <mat-icon *ngIf="data.type == SNACKBAR_TYPES.UPDATE">error</mat-icon>
        </div>
        <div class="message-panel">
          <span>{{data.message}}</span>
        </div>
        <button (click)="snackBarRef.dismissWithAction()">{{data.type == SNACKBAR_TYPES.UPDATE? "Update Now": "Close"}}</button>
      </div>

  `,
    imports: [NgIf, MatIcon]
})
export class PitSnackbarComponent {
  SNACKBAR_TYPES = PIT_SNACKBAR_TYPES;

  constructor(
      public snackBarRef: MatSnackBarRef<PitSnackbarComponent>,
      @Inject(MAT_SNACK_BAR_DATA) public data: PitSnackbarData
  ) {}
}
