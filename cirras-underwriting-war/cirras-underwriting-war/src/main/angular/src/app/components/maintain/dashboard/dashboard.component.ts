import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { UnderwritingYear, UnderwritingYearList } from 'src/app/conversion/models-maintenance';
import { BaseComponent } from '../../common/base/base.component';
import { DashboardComponentModel } from './dashboard.component.model';
import { addUwYear, loadUwYears} from 'src/app/store/maintenance/maintenance.actions';
import { MAINTENANCE_COMPONENT_ID } from 'src/app/store/maintenance/maintenance.state';

@Component({
  selector: 'cirras-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent  extends BaseComponent implements OnChanges   {

  @Input() underwritingYears: UnderwritingYearList

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
