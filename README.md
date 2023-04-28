# java11-spring-gradle-bigquery-reference


Made with:
- **IntelliJ IDEA 2023.1 (Ultimate Edition)**
- **openjdk 11.0.17**
- **Gradle 7.6.1**


---


## Install & build

<details>
<summary>Download, install, and initialize the gcloud SDK on your local machine</summary>

Refer to the <a href="https://cloud.google.com/sdk/docs/install#other_installation_options">`gcloud` CLI documentation</a> to complete this step.

Install the `gcloud` SDK to the user's home directory (e.g., `/Users/USERNAME/google-cloud-sdk`).

When it's finished installing, add the `gcloud` executable to your system's `$PATH` and run the command:

```shell
gcloud init
```

</details>


<details>
<summary>Application Default Credentials (ADC) usage</summary>

```shell
gcloud auth login
gcloud auth application-default login
```

</details>


<details>
<summary>Echo a GCP service account access token</summary>

Run this command to assign a GCP access token to an environment variable on your system:

```shell
export GOOGLE_APPLICATION_CREDENTIALS=$(gcloud auth print-access-token --impersonate-service-account='SA_EMAIL_ADDRESS')
```

**Replace the following**:
- `SA_EMAIL_ADDRESS`: the email address of the service account to impersonate.

Example:

```shell
export GOOGLE_APPLICATION_CREDENTIALS=$(gcloud auth print-access-token --impersonate-service-account='9644524330-compute@developer.gserviceaccount.com')
```

</details>


<details>
<summary>Activate GCP service account</summary>

```shell
gcloud auth activate-service-account --key-file=${GOOGLE_APPLICATION_CREDENTIALS}
```

**Replace the following**:
- `GOOGLE_APPLICATION_CREDENTIALS`: the user's service account key.

</details>


<details>
<summary>Set the active GCP project</summary>

```shell
gcloud config set project ${GCP_PROJECT_ID}
```

</details>


<details>
<summary>List available gcloud SDK components</summary>

```shell
gcloud components list
```

</details>


<details>
<summary>Update gcloud SDK components</summary>

```shell
gcloud components update
```

</details>


<details>
<summary>Build JAR</summary>

```shell
./gradlew clean build
```

```shell
./gradlew clean build -x test
```

```shell
./gradlew clean build testClasses -x test
```

</details>


<details>
<summary>Add manifest file</summary>

```shell
jar -cmvf \
  ./build/tmp/jar/MANIFEST.MF \
  ./build/libs/java11-spring-gradle-bigquery-reference-0.0.1-SNAPSHOT.jar \
  ./build/classes/java/main/org/squidmin/java/spring/gradle/bigquery/JavaSpringGradleBigQueryReferenceApplication.class
```

</details>


<details>
<summary>Build container image</summary>

```shell
docker build -t \
  GCP_PROJECT_ID=$GCP_PROJECT_ID \
  java11-spring-gradle-bigquery-reference .
```

Example:

```shell
docker build -t \
  GCP_PROJECT_ID=lofty-root-305785 \
  java11-spring-gradle-bigquery-reference .
```

</details>


---


## Commands


### Java application

<details>
<summary>Run container</summary>

```shell
docker run \
  --rm -it \
  -e GOOGLE_APPLICATION_CREDENTIALS=$GOOGLE_APPLICATION_CREDENTIALS \
  -v $HOME/.config/gcloud:/root/.config/gcloud \
  -v $HOME/.m2:/root/.m2 \
  java11-spring-gradle-bigquery-reference
```

</details>


<details>
<summary>Run jar</summary>

```shell
exec java -jar \
  -Dspring.profiles.active=local \
  ./build/libs/java11-spring-gradle-bigquery-reference-0.0.1-SNAPSHOT.jar
```

</details>


### `bq` CLI

<details>
<summary>List datasets</summary>

```shell
bq ls --filter labels.key:value \
  --max_results integer \
  --format=prettyjson \
  --project_id project_id
```

**Replace the following**:
- `key:value`: a label key and value, if applicable.
- `integer`: an integer representing the number of datasets to list.
- `project_id`: the name of the GCP project to target.

**Examples**:

```shell
bq ls --format=pretty
```

</details>


<details>
<summary>Create a dataset</summary>

Refer to the <a href="https://cloud.google.com/bigquery/docs/datasets#create-dataset">GCP documentation for creating datasets</a>.

**Examples**:

```shell
bq --location=us mk \
  --dataset \
  --default_partition_expiration=3600 \
  --default_table_expiration=3600 \
  --description="An example." \
  --label=test_label_1:test_value_1 \
  --label=test_label_2:test_value_2 \
  --max_time_travel_hours=168 \
  --storage_billing_model=LOGICAL \
  lofty-root-378503:test_dataset_name_lofty
```

The Cloud Key Management Service (KMS) key parameter (`KMS_KEY_NAME`) can be specified.
This parameter is used to pass the name of the default Cloud Key Management Service key used to protect newly created tables in this dataset.
You cannot create a Google-encrypted table in a dataset with this parameter set.

```shell
bq --location=us mk \
  --dataset \
  --default_kms_key=KMS_KEY_NAME \
  ...
  lofty-root-378503:test_dataset_name_lofty
```

</details>


<details>
<summary>Delete a dataset</summary>

Refer to the <a href="https://cloud.google.com/bigquery/docs/managing-datasets#delete_a_dataset">GCP documentation for deleting a dataset</a>.

#### Examples:

Remove all tables in the dataset (`-r` flag):

```shell
bq rm -r -f -d lofty-root-378503:test_dataset_name_lofty
```

</details>


<details>
<summary>Create a table with a configured schema</summary>

**Create an empty table with an inline schema definition**

```shell
bq mk --table project_id:dataset.table schema
```

**Replace the following**:
- `project_id`: the name of the GCP project to target.
- `dataset`: the name of the BigQuery dataset to target.
- `table`: the name of the BigQuery table to target.
- `schema`: an inline schema definition.

Example:

```shell
bq mk --table \
  lofty-root-378503:test_dataset_name_lofty.test_table_name_lofty \
  id:STRING,fieldA:STRING,fieldB:STRING,fieldC:STRING,fieldD:STRING
```

### Specify the schema in a JSON schema file

For an example JSON schema file, refer to: `/schema/example.json`.

**Create an empty table**

```shell
bq mk --table \
  project_id:dataset.table \
  path_to_schema_file
```

Example:

```shell
bq mk --table \
  lofty-root-378503:test_dataset_name_lofty.test_table_name_lofty \
  ./schema/example.json
```

**Create a table with CSV data**

```shell
bq --location=location load \
  --source_format=format \
  project_id:dataset.table \
  path_to_data_file \
  path_to_schema_file
```

Example:

```shell
bq --location=us load \
  --source_format=CSV \
  lofty-root-378503:test_dataset_name_lofty.test_table_name_lofty \
  ./csv/example.csv \
  ./schema/example.json
```

Refer to the BigQuery documentation: <a href="https://cloud.google.com/bigquery/docs/loading-data-cloud-storage-csv#details_of_loading_csv_data">Details of loading CSV data</a>.

</details>


<details>
<summary>Delete a table</summary>

```shell
bq rm --table test_dataset_name_lofty.test_table_name_lofty
```

</details>


<details>
<summary>Show table schema</summary>

Example:

```shell
bq show \
  --schema \
  --format=prettyjson \
  lofty-root-378503:test_dataset_name_lofty.test_table_name_lofty
```

The table schema can be written to a file:

```shell
bq show \
  --schema \
  --format=prettyjson \
  lofty-root-378503:test_dataset_name_lofty.test_table_name_lofty \ > ./schema/example_show-write.json
```

</details>


<details>
<summary>Modify table schemas</summary>

```shell
bq update \
  lofty-root-378503:test_dataset_name_lofty.test_table_name_lofty \
  ./schema/example_update.json
```

Refer to the <a href="https://cloud.google.com/bigquery/docs/managing-table-schemas">GCP documentation on modifying table schemas.</a>.

</details>


<details>
<summary>Insert data into a table</summary>

**Examples**:

Insert for known values:

```shell
bq insert test_dataset_name_lofty.test_table_name_lofty ./json/example.json
```

Specify a template suffix (`--template_suffix` or `-x`):

```shell
bq insert --ignore_unknown_values \
  --template_suffix=_insert \
  test_dataset_name_lofty.test_table_name_lofty \
  ./json/example.json
```

Refer to the <a href="">`bq insert` documentation</a>.

</details>


<details>
<summary>Run an interactive query</summary>

```shell
bq query \
  --use_legacy_sql=false \
  'query_string'
```

Example:

```shell
bq query \
  --use_legacy_sql=false \
  'SELECT
    id, fieldC
  FROM
    `lofty-root-378503.test_dataset_name_lofty.test_table_name_lofty`
  LIMIT
    3;'
```

</details>
