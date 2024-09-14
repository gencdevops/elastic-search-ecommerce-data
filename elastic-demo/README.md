# CRUD operation using ElasticSearch 

# Technologies
Java, 
Spring Boot v2.2.0.RELEASE (Micro services) ,Spring-Security, 
MySql (for rest authentication & insert data to generate id),
JPA, Maven
# Download elastic search from 
https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.8.3.zip , 
upzip & start. example open file in windows(\elasticsearch-6.8.3\bin\elasticsearch.bat)

# Create index using postman OR code will create automatically
Method:PUT URL: http://localhost:9200/employee-data // {employee-data} is index name 

# Use below REST services for CRUD Operation
# 1. CREATE Method:POST 
http://localhost:8080/spring-boot-elastic-search/employee/add
{
	"lastName": "ooasssssss",
	"firstName": "aaaaaaaa",
	"email":"sssaa@gmail.com"
} 
# 2. UPDATE Method:POST 
http://localhost:8080/spring-boot-elastic-search/employee/update/2
{
	"lastName": "ooasssssss", 
	"firstName": "aaaaaaaa",
	"email":"sssaa@gmail.com"
} 
# 3. DELETE Method:GET/POST 
http://localhost:8080/spring-boot-elastic-search/employee/delete/2
# 4. GET By Id Method:GET/POST 
http://localhost:8080/spring-boot-elastic-search/employee/get/2
# 4. GET All Record Method:GET/POST 
http://localhost:8080/spring-boot-elastic-search/employee/getAll
