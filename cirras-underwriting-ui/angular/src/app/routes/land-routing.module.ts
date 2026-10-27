import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ResourcesAuthGuard } from '../services/util/ResourcesAuthGuard';
import { DeactivateGuard } from '../services/util/DeactivateGuard';
import { LandListContainer } from '../containers/land-management/land-list-container.component';
import { ROUTE_SCOPES } from '../utils/scopes';
import { ManageLandComponentContainer } from '../containers/land-management/manage-land-container.component';
import { ResourcesRoutes } from '../utils/constants';

const routes: Routes = [

    {
     path: ResourcesRoutes.LAND_LIST.split('/')[1], // -> 'land-list', 
     component: LandListContainer, 
     data: {scopes: [ROUTE_SCOPES.LEGAL_LAND]}, 
     pathMatch: "full"    
    }, 
    { path: ResourcesRoutes.LAND_MANAGE.split('/')[1], // -> 'manage-land'
        children: [
        { 
            path: ':legalLandId', component: ManageLandComponentContainer, data: {scopes: [ROUTE_SCOPES.LEGAL_LAND]},
            canActivate: [ResourcesAuthGuard],
            canDeactivate: [DeactivateGuard]
        },
        ] 
    },

];

@NgModule({
    imports: [
      CommonModule,
      RouterModule.forChild(routes),
      LandListContainer,
      ManageLandComponentContainer
    ]
})

export class LandRoutingModule {}