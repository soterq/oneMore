import { Component, OnInit } from '@angular/core';

import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AuthService } from '../service/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  [x: string]: any;
  loginForm: FormGroup
  errorMessage = ""

  constructor(private formBuilder:FormBuilder,
    private authService:AuthService, 
    private router:Router) { }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username:['', Validators.required],
      password:['',Validators.required]
    });
  }

  get f(){
    return this.loginForm.controls;
  }

  login() {
    // to add validator and regex
    this.authService.login(
      {
        username: this.f.username.value,
        password: this.f.password.value
      }
    )
    .subscribe(success => {
      if (success) {
        //this.router.navigate(['/secret-random-number']);
        this.router.navigate(['/testtable']);
      }
    });
  }
}
