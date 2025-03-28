#!/usr/bin/env python3
import pandas as pd
import matplotlib.pyplot as plt
import io
import re
import sys

report = sys.argv[1]

# Read the CSV data
df = pd.read_csv(f"reports/{report}.csv", header=None, sep=',')

# Convert timestamp to numeric
df[0] = pd.to_numeric(df[0])

# Calculate relative time starting from 0
df['relative_time'] = df[0] - df[0].iloc[0]

# Function to parse memory string and convert to MiB
def parse_memory(memory_str):
    match = re.match(r'(\d+\.?\d+)(MiB|GiB)\s*/', memory_str)
    if match:
        value = float(match.group(1))
        unit = match.group(2)
        if unit == 'GiB':
            return value * 1024
        elif unit == 'MiB':
            return value
    return None

# Apply the function to the memory column
df['memory_mib'] = df[1].apply(parse_memory)

# Extract CPU usage and convert to float
df['cpu_percent'] = df[2].str.replace('%', '').astype(float)

# Create the plot
fig, ax1 = plt.subplots(figsize=(10, 6))

# Plot memory usage on the left y-axis
color = 'tab:blue'
ax1.set_xlabel('Time (seconds)')
ax1.set_ylabel('Memory Usage (MiB)', color=color)
ax1.plot(df['relative_time'], df['memory_mib'], color=color, label='Memory Usage')
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

# Show the plot
# plt.grid(True)
# plt.show()

# Specify the filename for saving the plot
plot_filename = f"reports/{report}.png"

# Save the plot to a file
plt.savefig(plot_filename)
