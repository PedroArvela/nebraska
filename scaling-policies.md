# Scaling policies

- Decrease Group Size	
  - Execute policy when: **CPUUtilization <= 30** for 2 consecutive periods of 60 seconds
  - Take the action: remove **50%** of group
- Increase Group Size
  - Execute policy when: **CPUUtilization >= 80** for 60 seconds
  - Take the action: Add **25%** of group

Instances need: 300 seconds to warm up after each step
