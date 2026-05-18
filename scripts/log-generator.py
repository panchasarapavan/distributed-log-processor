import requests
import random
import time
import datetime
import json
import argparse

def generate_log():
    org_ids = ["org-1", "org-2", "org-3", "org-4", "org-5"]
    levels = ["INFO", "WARN", "ERROR", "DEBUG"]
    sources = ["web-server", "auth-service", "payment-gateway", "database", "inventory-service", "frontend-app"]
    messages = [
        "User login successful",
        "Database connection timeout",
        "Invalid API key provided",
        "Cache hit for user profile",
        "Processing order #12345",
        "Failed to send email notification",
        "Memory usage exceeding threshold",
        "New user registered: john.doe",
        "File uploaded: report.pdf",
        "Heartbeat check: service healthy",
        "Stock level low for item SKU-99",
        "External API call failed (re-trying)",
        "Config updated: log-level set to DEBUG",
        "User john.doe updated profile picture",
        "Scheduled task 'cleanup-tmp' finished"
    ]

    return {
        "organizationId": random.choice(org_ids),
        "level": random.choice(levels),
        "source": random.choice(sources),
        "message": random.choice(messages),
        "timestamp": datetime.datetime.now().strftime("%Y-%m-%dT%H:%M:%S")
    }

def main():
    parser = argparse.ArgumentParser(description="Distributed Log Processor Generator")
    parser.add_argument("--url", default="http://localhost:8080/api/logs", help="Target URL (default: API Gateway)")
    parser.add_argument("--rate", type=float, default=10.0, help="Target requests per second (default: 10)")
    parser.add_argument("--total", type=int, default=100, help="Total logs to send (default: 100)")
    
    args = parser.parse_args()

    print("=" * 40)
    print("   Distributed Log Processor Generator   ")
    print("=" * 40)
    print(f"Target URL: {args.url}")
    print(f"Rate limit: {args.rate} requests per second")
    print(f"Total logs: {args.total}")
    print("-" * 40)

    count = 0
    start_time = time.time()
    
    # Calculate delay between requests to maintain rate
    delay = 1.0 / args.rate if args.rate > 0 else 0

    while count < args.total:
        log_event = generate_log()
        
        try:
            response = requests.post(args.url, json=log_event, timeout=5)
            
            if response.status_code == 200 or response.status_code == 201:
                count += 1
                if count % 10 == 0:
                    elapsed = time.time() - start_time
                    rps = round(count / elapsed, 2)
                    print(f"Progress: {count}/{args.total} sent. Current rate: {rps} logs/sec")
            elif response.status_code == 429:
                print(f"Rate limit hit at log {count}! Waiting 1s...")
                time.sleep(1)
            elif response.status_code == 503:
                print(f"Service Unavailable (503) at log {count}! Waiting 2s...")
                time.sleep(2)
            else:
                print(f"Unexpected status {response.status_code}: {response.text}")
                time.sleep(1)
                
        except requests.exceptions.RequestException as e:
            print(f"Request failed: {e}")
            time.sleep(1)

        # Maintain rate limit
        time.sleep(delay)

    end_time = time.time()
    duration = end_time - start_time
    print("-" * 40)
    print(f"Finished! Sent {count} logs in {round(duration, 2)} seconds.")
    print(f"Average Rate: {round(count / duration, 2)} logs/sec")

if __name__ == "__main__":
    main()
