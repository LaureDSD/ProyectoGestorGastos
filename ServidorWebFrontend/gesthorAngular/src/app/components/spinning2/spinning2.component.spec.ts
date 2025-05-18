import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Spinning2Component } from './spinning2.component';

describe('Spinning2Component', () => {
  let component: Spinning2Component;
  let fixture: ComponentFixture<Spinning2Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Spinning2Component]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Spinning2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
