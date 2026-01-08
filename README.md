# ReadMe for UserService
This service does not do anything much at all at the moment. It will be updated as I feel appropriate.

Run this service with ./mvnw spring-boot:run

## Currently Available APIs
Read MessageController.kt for all currently available APIs.

## Features
### Message Sentiment Analysis
Messages can now be tagged with a sentiment (UNSPECIFIED or HAPPY).

When a message is sent with sentiment `HAPPY`, the service verifies this sentiment using an external LLM API (OpenAI).
- If the LLM confirms the message is happy, the sentiment is saved as `HAPPY`.
- If the LLM determines the message is not happy (or if the API call fails), the sentiment is saved as `UNSPECIFIED`.

### LLM Configuration
To enable the sentiment verification, you must provide an OpenAI API key.

By default, the service looks for a file named `llm_api_key.txt` in the root directory of the application.
You can create this file and paste your API key into it:
```bash
echo "your-openai-api-key" > llm_api_key.txt
```

Alternatively, you can configure the path to the key file or the API URL via application properties:
- `llm.api.key.path`: Path to the file containing the API key (default: `llm_api_key.txt`).
- `llm.api.url`: URL of the LLM API (default: `https://api.openai.com/v1/chat/completions`).
- `llm.model`: Model to use (default: `gpt-4o-mini`).

## Loading Data (Messages) With Python Script
To load messages into the service, you can use the `load-messages.py` script.

The script supports a CSV file with one or two columns:
1. Message text
2. Sentiment (optional)

Example CSV:
```csv
Hello world,HAPPY
I am sad
Test message,UNSPECIFIED
```

First, install the required Python dependencies:
```bash
pip install -r scripts/requirements.txt
```

Then, run the script with the path to your CSV file:
```bash
python scripts/load-messages.py <path-to-csv-file>
```

## Known Issues
The application is not yet interesting enough to have notable known issues.

## Important Documentation
- https://docs.spring.io/spring-boot/appendix/application-properties/index.html
