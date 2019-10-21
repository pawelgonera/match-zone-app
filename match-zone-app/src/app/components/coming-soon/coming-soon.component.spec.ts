import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CommingSoonComponent } from './coming-soon.component';

describe('CommingSoonComponent', () => {
  let component: CommingSoonComponent;
  let fixture: ComponentFixture<CommingSoonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CommingSoonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CommingSoonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
