import { Component } from '@angular/core';
import { from } from 'rxjs';
import {AxiosService} from '../axios.service';


@Component({
  selector: 'app-auth-content',
  templateUrl: './auth-content.component.html',
  styleUrls: ['./auth-content.component.scss']
})
export class AuthContentComponent {

  data : string[] = []; //stock data response


  constructor( private axiosservice : AxiosService){

  }

  ngOnInit(): void {
    this.axiosservice.request(
        "GET",
        "/messages",
        {}).then(
        (response) => {
            this.data = response.data;
        })
        
    
  }
}
