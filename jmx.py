#!/usr/bin/env python3
from jmxquery import JMXConnection, JMXQuery
import csv
import sys
import signal
import time

address, file_name = sys.argv[1:]

jmx_url = f"service:jmx:rmi:///jndi/rmi://{address}/jmxrmi"
connection = JMXConnection(jmx_url)
heap_memory_query = JMXQuery('java.lang:type=Memory', 'HeapMemoryUsage')
cpu_query = JMXQuery('java.lang:type=OperatingSystem', 'ProcessCpuLoad')

# Global variable to hold the file object
output_file = None
running = True

def signal_handler(sig, frame):
    global running
    print("\nSIGINT received, flushing data and exiting...")
    if output_file:
        output_file.flush()
        output_file.close()
    running = False
    sys.exit(0)

signal.signal(signal.SIGINT, signal_handler)

try:
    with open(file_name, 'w') as f:
        while running:
            results = [it.__dict__ for it in connection.query([heap_memory_query, cpu_query])]
            row = [int(time.time())] + [round(r['value'], 4) for r in results if r['attributeKey'] in ['used', None]]
            f.write(','.join(map(str, row)) + '\n')
            time.sleep(1)
except:
    pass
finally:
    if output_file and not output_file.closed:
        output_file.flush()
        output_file.close()