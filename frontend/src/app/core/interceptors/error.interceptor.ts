import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../auth/auth.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const authService = inject(AuthService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        console.warn('[INTERCEPTOR 401] Sesión caducada o no autorizada.');
        authService.logout();
        router.navigate(['/login']);
      } else if (error.status === 403) {
        console.error('[INTERCEPTOR 403] Acceso prohibido: Privilegios de rol insuficientes.');
      } else if (error.status >= 500) {
        console.error('[INTERCEPTOR 500] Error interno en servidor Spring Boot:', error.message);
      }
      return throwError(() => error);
    })
  );
};
