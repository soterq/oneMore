import { Injectable } from '@angular/core';

import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { SimBox } from '../navigation/SimBox';

@Injectable({
  providedIn: 'root'
})
export class SimBoxListService {

  constructor(private http:HttpClient) { }

  getSimBoxsList() {
    return this.http.get(`${environment.apiUrl}/testtable`)
                .toPromise()
                .then(res => <SimBox[]> res)
                .then(data => { 
                  console.log(data);
                  return data; });
}
}
