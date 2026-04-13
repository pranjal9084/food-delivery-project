curl -s -X POST http://localhost:8080/auth/signup -H "Content-Type: application/json" -d '{"email":"test@test.com","password":"password123","fullName":"Test User","role":"ROLE_CUSTOMER"}' > signup.json
echo "Signup:"
cat signup.json
echo ""

TOKEN=$(grep -o '"jwt":"[^"]*' signup.json | cut -d'"' -f4)
echo "Token: $TOKEN"

echo "Profile:"
curl -s -X GET http://localhost:8080/api/users/profile -H "Authorization: Bearer $TOKEN"
