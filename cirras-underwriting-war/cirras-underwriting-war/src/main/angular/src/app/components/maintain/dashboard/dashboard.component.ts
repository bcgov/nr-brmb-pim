import { ChangeDetectorRef, Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { UnderwritingYear, UnderwritingYearList } from 'src/app/conversion/models-maintenance';
import { BaseComponent } from '../../common/base/base.component';
import { DashboardComponentModel } from './dashboard.component.model';
import { FormArray, FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { addUwYear, loadUwYears} from 'src/app/store/maintenance/maintenance.actions';
import { MAINTENANCE_COMPONENT_ID } from 'src/app/store/maintenance/maintenance.state';
import { ActivatedRoute, Router } from '@angular/router';
import { DomSanitizer, Title } from '@angular/platform-browser';
import { Store } from '@ngrx/store';
import { RootState } from 'src/app/store';
import { MatDialog } from '@angular/material/dialog';
import { ApplicationStateService } from 'src/app/services/application-state.service';
import { SecurityUtilService } from 'src/app/services/security-util.service';
import { AppConfigService, TokenService } from '@wf1/core-ui';
import { ConnectionService } from 'ngx-connection-service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Overlay } from '@angular/cdk/overlay';
import { HttpClient } from '@angular/common/http';
import { DecimalPipe } from '@angular/common';


@Component({
  selector: 'cirras-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent  extends BaseComponent implements OnChanges   {

  @Input() underwritingYears: UnderwritingYearList

  constructor(protected router: Router,
    protected route: ActivatedRoute,
    protected sanitizer: DomSanitizer,
    protected store: Store<RootState>,
    protected fb: FormBuilder,
    protected dialog: MatDialog,
    protected applicationStateService: ApplicationStateService,
    public securityUtilService: SecurityUtilService,                
    protected tokenService: TokenService,
    protected connectionService: ConnectionService,
    protected snackbarService: MatSnackBar,
    protected overlay: Overlay,
    protected cdr: ChangeDetectorRef,
    protected appConfigService: AppConfigService,
    protected http: HttpClient,
    protected titleService: Title,
    protected decimalPipe: DecimalPipe) {
    super(router, route, sanitizer, store, fb, dialog, applicationStateService, securityUtilService, tokenService, connectionService, snackbarService, overlay, cdr, appConfigService, http, titleService, decimalPipe);
  }

  uwYearOptions = [];

  initModels() {
    this.viewModel = new DashboardComponentModel(this.sanitizer, this.fb);
  }

  getViewModel(): DashboardComponentModel  { //
    return <DashboardComponentModel>this.viewModel;
  }

  loadPage() {
    this.loadUwCropYear()
  }

  ngOnChanges(changes: SimpleChanges): void {
  
    var self = this;

    if ( changes.underwritingYears && this.underwritingYears && this.underwritingYears.collection && this.underwritingYears.collection.length > 0 ) {
      // populate the crop year dropdown
      self.uwYearOptions = []

      this.underwritingYears.collection.forEach (x => 
        self.uwYearOptions.push ({
          cropYear: x.cropYear
        }))

    }
  
  }

  loadUwCropYear() {
    if (!this.underwritingYears) {
      this.store.dispatch(loadUwYears(MAINTENANCE_COMPONENT_ID))  
    }
  }

  onCreateNewUwYear() {

    if (this.underwritingYears) {
      //Get max year
      var maxValue = Math.max.apply(null, this.underwritingYears.collection.map(function (o) { return o.cropYear; }));

      if(maxValue){
        var newCropYear = maxValue + 1;

        let newUwYear : UnderwritingYear

        newUwYear = {
          links: [],
          underwritingYearGuid: null,
          cropYear: newCropYear,
          etag: "",
          type: ""
        }

        this.store.dispatch(addUwYear(MAINTENANCE_COMPONENT_ID, newUwYear))

      } else {
        alert("Error finding max year")
      }
    }    
  }


}
