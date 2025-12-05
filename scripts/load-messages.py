import requests
import csv
import sys

def send_message(text):
    """Sends a single message to the API."""
    url = "http://localhost:8080"
    headers = {"Content-Type": "application/json"}
    data = {"text": text}
    try:
        response = requests.post(url, headers=headers, json=data)
        response.raise_for_status()  # Raise an exception for bad status codes
        print(f"Successfully sent message: {text}")
        return response.json()
    except requests.exceptions.RequestException as e:
        print(f"Error sending message: {text}. Error: {e}")
        return None

def main(csv_file_path):
    """Reads messages from a CSV file and attempts to send Messages to localhost:8080"""
    try:
        with open(csv_file_path, 'r', encoding='utf-8', newline='') as file:
            reader = csv.reader(file) # TODO: switch to csv.DictReader in future.
            for row in reader:
                if row:
                    send_message(row[0])
    except FileNotFoundError:
        print(f"Error: The file '{csv_file_path}' was not found.")
    except Exception as e:
        print(f"An unexpected error occurred: {e}")


if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python load-messages.py <path_to_csv_file>")
        sys.exit(1)
    
    csv_path = sys.argv[1]
    main(csv_path)
