# ReadMe for UserService
This service does not do anything much at all at the moment. It will be updated as I feel appropriate.

Run this service with ./mvnw spring-boot:run

## Currently Available APIs
Read MessageController.kt for all currently available APIs.

## Message Sentiment Analysis and Gemini API Key
You will need a Gemini API Key for message sentiment analysis to work. See: https://aistudio.google.com/app/api-keys

Your key should be saved at the appropriate line in application.properties

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

If you want to include sentiment and not use Gemini for sentiment analysis, use the second column of your .csv file to add a sentiment. (See Sentiment.kt)

## Known Issues
The application is not yet interesting enough to have notable known issues.

## Important Documentation
- https://docs.spring.io/spring-boot/appendix/application-properties/index.html

## Code Style & Formatting
This project uses **ktlint** for consistent code formatting.

To automatically format the codebase, run:
```bash
./mvnw ktlint:format
```

To check for style violations without applying fixes, run:
```bash
./mvnw ktlint:check
```