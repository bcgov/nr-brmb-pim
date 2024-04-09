import {HttpClient} from '@angular/common/http';
import {Injectable} from "@angular/core";
import {Actions} from "@ngrx/effects";
import {Store} from "@ngrx/store";
import {RootState} from "../index";
import {ApplicationStateService} from "../../services/application-state.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {AppConfigService, TokenService} from "@wf1/core-ui";
@Injectable()
export class ApplicationEffects {
  constructor(
      private actions: Actions,
      private store: Store<RootState>,
      private snackbarService: MatSnackBar,
      private tokenService: TokenService,
      private applicationStateService: ApplicationStateService,
      private httpClient: HttpClient,
      private appConfigService: AppConfigService) {
  }
}
