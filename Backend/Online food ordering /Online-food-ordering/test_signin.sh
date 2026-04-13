echo "Signin:"
curl -s -X POST http://localhost:8080/auth/signin -H "Content-Type: application/json" -d '{"email":"test@test.com","password":"password123"}'
