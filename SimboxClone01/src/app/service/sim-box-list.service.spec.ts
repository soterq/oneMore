import { TestBed } from '@angular/core/testing';

import { SimBoxListService } from './sim-box-list.service';

describe('SimBoxListService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: SimBoxListService = TestBed.get(SimBoxListService);
    expect(service).toBeTruthy();
  });
});
