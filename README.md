# java-web-benchmark

## How to build
Requires java21 + docker:
```
./build.sh
```

## How to run
```
dd if=/dev/zero of=file.dat  bs=10M  count=1 # needed for docker compose
docker compose up -d
```

## Running benchmark
```
python3 -m venv .venv
pip install -r requirements.txt
source .venv/bin/activate
./script.sh
```