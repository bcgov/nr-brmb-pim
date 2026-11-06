import {Component, Inject} from "@angular/core";
// External
import {MAT_SNACK_BAR_DATA, MatSnackBarRef} from "@angular/material/snack-bar";
import {PIT_SNACKBAR_TYPES, PitSnackbarData} from "../../../utils";

import { MatIcon } from "@angular/material/icon";

@Component({
    selector: 'cirras-underwriting-snackbar',
    template: `
      <div class="snack-bar-content">
        <div class="icon-panel">
          @if (data.type == SNACKBAR_TYPES.SUCCESS) {
            <mat-icon>check_circle</mat-icon>
          }
          @if (data.type == SNACKBAR_TYPES.ERROR) {
            <mat-icon>cancel</mat-icon>
          }
          @if (data.type == SNACKBAR_TYPES.WARNING) {
            <mat-icon>info</mat-icon>
          }
          @if (data.type == SNACKBAR_TYPES.UPDATE) {
            <mat-icon>error</mat-icon>
          }
        </div>
        <div class="message-panel">
          <span>{{data.message}}</span>
        </div>
        <button (click)="snackBarRef.dismissWithAction()">{{data.type == SNACKBAR_TYPES.UPDATE? "Update Now": "Close"}}</button>
      </div>
      
      `,
    imports: [MatIcon]
})
export class PitSnackbarComponent {
  SNACKBAR_TYPES = PIT_SNACKBAR_TYPES;

  constructor(
      public snackBarRef: MatSnackBarRef<PitSnackbarComponent>,
      @Inject(MAT_SNACK_BAR_DATA) public data: PitSnackbarData
  ) {}
}
