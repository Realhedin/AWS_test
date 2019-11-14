To make it work, following actions need to be done:
1) create IAM user, group, policy (for S3 - Full Access)
https://console.aws.amazon.com/iam/home
That needs to have an access from API

2) After creating user,
save Access key ID and Secret access key
to C:\Users\USERNAME\.aws\credentials
content of credentials:
[default]
aws_access_key_id = your_access_key_id
aws_secret_access_key = your_secret_access_key
3) set these variables
set AWS_ACCESS_KEY_ID=your_access_key_id
set AWS_SECRET_ACCESS_KEY=your_secret_access_key

4) create a C:\Users\USERNAME\.aws\config
content of config:
[default]
region = your_aws_region
5) set this variable
set AWS_REGION=your_aws_region