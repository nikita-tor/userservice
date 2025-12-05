# ReadMe for UserService
This service does not do anything much at all at the moment. It will be updated as I feel appropriate.

Run this service with ./mvnw spring-boot:run

## Currently Available APIs
Read MessageController.kt for all currently available APIs.

## Loading Data (Messages) With Python Script
To load messages into the service, you can use the `load-messages.py` script.

First, install the required Python dependencies:
```bash
pip install -r scripts/requirements.txt
```

Then, run the script with the path to your CSV file:
```bash
python scripts/load-messages.py <path-to-csv-file>
```

## Important Documentation
- https://docs.spring.io/spring-boot/appendix/application-properties/index.html