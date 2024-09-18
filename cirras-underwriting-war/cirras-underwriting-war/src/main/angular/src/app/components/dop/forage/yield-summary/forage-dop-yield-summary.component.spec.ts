import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ForageDopYieldSummaryComponent } from './forage-dop-yield-summary.component';

describe('ForageDopYieldSummaryComponent', () => {
  let component: ForageDopYieldSummaryComponent;
  let fixture: ComponentFixture<ForageDopYieldSummaryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ForageDopYieldSummaryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ForageDopYieldSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
