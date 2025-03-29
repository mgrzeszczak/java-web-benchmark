#!/usr/bin/env python3
import pandas as pd
import matplotlib.pyplot as plt
import sys

report = sys.argv[1]

# Read the CSV data
df = pd.read_csv(f"reports/{report}.csv", header=None, sep=',')

# Rename columns for clarity
df.columns = ['unix_timestamp', 'memory_bytes', 'cpu']

# Convert timestamp to numeric
df['unix_timestamp'] = pd.to_numeric(df['unix_timestamp'])

# Calculate relative time starting from 0
df['relative_time'] = df['unix_timestamp'] - df['unix_timestamp'].iloc[0]

# Convert memory from bytes to megabytes
df['memory_mb'] = df['memory_bytes'] / (1024 * 1024)

# Convert CPU usage to percentage
df['cpu_percent'] = df['cpu'] * 100

# Create the plot
fig, ax1 = plt.subplots(figsize=(10, 6))

# Plot memory usage on the left y-axis
color = 'tab:blue'
ax1.set_xlabel('Time (seconds)')
ax1.set_ylabel('Memory Usage (MB)', color=color)
ax1.plot(df['relative_time'], df['memory_mb'], color=color, label='Memory Usage')
ax1.tick_params(axis='y', labelcolor=color)

# Create a second y-axis for CPU usage
ax2 = ax1.twinx()
color = 'tab:red'
ax2.set_ylabel('CPU Usage (%)', color=color)
ax2.plot(df['relative_time'], df['cpu_percent'], color=color, label='CPU Usage')
ax2.tick_params(axis='y', labelcolor=color)

# Add title and legend
plt.title('Memory and CPU Usage Over Time')
fig.tight_layout()  # Adjust layout to prevent labels from overlapping

# Combine legends from both axes
lines, labels = ax1.get_legend_handles_labels()
lines2, labels2 = ax2.get_legend_handles_labels()
ax2.legend(lines + lines2, labels + labels2, loc='upper left')

# Specify the filename for saving the plot
plot_filename = f"reports/{report}.png"

# Save the plot to a file
plt.savefig(plot_filename)