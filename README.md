![Clients App - Thumbnail](https://github.com/ReevesKKO/Clients-mobile-application-PSC/assets/53497618/3816194d-1538-4f62-b3b3-a46f049485d3)
Works only with [PSC-backend](https://github.com/ReevesKKO/PSC-backend).

Also check out [Employees-mobile-application-PSC](https://github.com/ReevesKKO/Employees-mobile-application-PSC).

## How it is supposed to work:
```mermaid
graph LR
A[Database - MySQL 8.0] --> B[PSC-backend - PHP 8.1] 
B-->A
B-->C[Clients-mobile-application-PSC - Client, Android App] 
C-->B
B-->D[Employees-mobile-application-PSC - Client, Android App]
D-->B
```
