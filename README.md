# spring-rest-labs


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
<summary>Build</summary>

```shell
./gradlew build
```

or

```shell
./gradlew build -x test
```

or

```shell
./gradlew build testClasses -x test
```

</details>


---


## Commands


### Java application

<details>
<summary>WIP</summary>



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

**Create and empty table with an inline schema definition**

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

</details>


<details>
<summary>Delete a table</summary>

```shell
bq rm --table test_dataset_name_lofty.test_table_name_lofty
```

</details>


---


## Tests

### REST controller

<details>
<summary>Run the "findById" endpoint test</summary>

```shell
./gradlew [ cleanTest ] test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.controller.ControllerTest.lookUpById_givenClientRequest_whenCalled_thenReturn200 \
  -Dprofile=integration \
  -DprojectId="lofty-root-378503" \
  -DdatasetName="test_dataset_name_integration" \
  -DtableName="test_table_name_integration" \
  -Did=asdf-1234
```

</details>


### Table admin

<details>
<summary>List BigQuery resource metadata configured for a particular Spring profile</summary>

```shell
./gradlew [ cleanTest ] test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryAdminClientIntegrationTest.echoDefaultBigQueryResourceMetadata \
  -Dprofile=PROFILE_NAME \
  -DprojectId="PROJECT_ID"
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate for the method execution.
- `PROJECT_ID`: the project ID for the GCP project to target.

For example, assuming the name of the profile to activate is `integration`:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryAdminClientIntegrationTest.echoDefaultBigQueryResourceMetadata \
  -Dprofile=integration \
  -DprojectId=lofty-root-378503
```

</details>


<details>
<summary>List datasets</summary>

```shell
./gradlew [ cleanTest ] test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryAdminClientIntegrationTest.listDatasets \
  -Dprofile=PROFILE_NAME \
  -DprojectId="PROJECT_ID"
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate.
- `PROJECT_ID`: the project ID of the GCP project to target.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryAdminClientIntegrationTest.listDatasets \
  -Dprofile=integration \
  -DprojectId="lofty-root-378503"
```

</details>


<details>
<summary>Check whether a dataset exists</summary>

```shell
./gradlew [ cleanTest ] test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryAdminClientIntegrationTest.datasetExists \
  -Dprofile=PROFILE_NAME \
  -DprojectId="PROJECT_ID" \
  -DdatasetName="DATASET_NAME"
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate.
- `PROJECT_ID`: the project ID of the GCP project to target.
- `DATASET_NAME`: the name of the dataset to target.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryAdminClientIntegrationTest.datasetExists \
  -Dprofile=integration \
  -DprojectId="lofty-root-378503" \
  -DdatasetName="test_dataset_name_integration"
```

</details>


<details>
<summary>Create a dataset</summary>

```shell
./gradlew [ cleanTest ] test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryAdminClientIntegrationTest.createDataset \
  -Dprofile=PROFILE_NAME \
  -DprojectId="PROJECT_ID" \
  -DdatasetName="DATASET_NAME"
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate.
- `PROJECT_ID`: the project ID of the GCP project to target.
- `DATASET_NAME`: the name of the dataset to target.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryAdminClientIntegrationTest.createDataset \
  -Dprofile=integration \
  -DprojectId="lofty-root-378503" \
  -DdatasetName="test_dataset_name_integration"
```

</details>


<details>
<summary>Delete a dataset</summary>

```shell
./gradlew [ cleanTest ] test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryAdminClientIntegrationTest.deleteDataset \
  -Dprofile=PROFILE_NAME \
  -DprojectId="PROJECT_ID" \
  -DdatasetName="DATASET_NAME"
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate.
- `PROJECT_ID`: the project ID of the GCP project to target.
- `DATASET_NAME`: the name of the dataset to target.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryAdminClientIntegrationTest.deleteDataset \
  -Dprofile=integration \
  -DprojectId="lofty-root-378503" \
  -DdatasetName="test_dataset_name_integration"
```

</details>


<details>
<summary>Delete dataset and contents</summary>

```shell
./gradlew [ cleanTest ] test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryAdminClientIntegrationTest.deleteDatasetAndContents \
  -Dprofile=PROFILE_NAME \
  -DprojectId="PROJECT_ID" \
  -DdatasetName="DATASET_NAME"
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate.
- `PROJECT_ID`: the project ID of the GCP project to target.
- `DATASET_NAME`: the name of the dataset to target.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryAdminClientIntegrationTest.deleteDatasetAndContents \
  -Dprofile=integration \
  -DprojectId="lofty-root-378503" \
  -DdatasetName="test_dataset_name_integration"
```

</details>


<details>
<summary>Create a table with the default schema</summary>

This command creates a table using the default schema configured in the Spring application.

```shell
./gradlew [ cleanTest ] test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryAdminClientIntegrationTest.createTableWithDefaultSchema \
  -Dprofile=PROFILE_NAME \
  -DprojectId="PROJECT_ID" \
  -DdatasetName="DATASET_NAME" \
  -DtableName="TABLE_NAME"
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate.
- `PROJECT_ID`: the project ID of the GCP project to target.
- `DATASET_NAME`: the name of the BigQuery dataset to target.
- `TABLE_NAME`: the name of the BigQuery table to target.

Example using the `integration` profile:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryAdminClientIntegrationTest.createTableWithDefaultSchema \
  -Dprofile=integration \
  -DprojectId="lofty-root-378503" \
  -DdatasetName="test_dataset_name_integration" \
  -DtableName="test_table_name_integration"
```

</details>


<details>
<summary>Create a table with a configured schema</summary>

```shell
./gradlew [ cleanTest ] test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryAdminClientIntegrationTest.createTableWithCustomSchema \
  -Dprofile=PROFILE_NAME \
  -DprojectId="PROJECT_ID" \
  -DdatasetName="DATASET_NAME" \
  -DtableName="TABLE_NAME" \
  -Dschema="name_1,datatype_1;name_2,datatype_2;[...];name_n,datatype_n"
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate.
- `PROJECT_ID`: the name of the GCP project ID to target.
- `DATASET_NAME`: the name of the BigQuery dataset to target.
- `TABLE_NAME`: the name of the BigQuery table to target.
- `name_1,datatype_1;name_2,datatype_2;[...];name_n,datatype_n`: a basic representation of a database schema.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryAdminClientIntegrationTest.createTableWithCustomSchema \
  -Dprofile=integration \
  -DprojectId="lofty-root-378503" \
  -DdatasetName="test_dataset_name_integration" \
  -DtableName="test_table_name_integration" \
  -Dschema="id,string;fieldA,string;fieldB,string;fieldC,string;fieldD,string"
```

</details>


<details>
<summary>Delete a table</summary>

```shell
./gradlew [ cleanTest ] test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryAdminClientIntegrationTest.deleteTable \
  -Dprofile=PROFILE_NAME \
  -DprojectId="PROJECT_ID" \
  -DdatasetName="DATASET_NAME" \
  -DtableName="TABLE_NAME"
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate.
- `PROJECT_ID`: the name of the GCP project ID to target.
- `DATASET_NAME`: the name of the BigQuery dataset to target.
- `TABLE_NAME`: the name of the BigQuery table to target.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryAdminClientIntegrationTest.deleteTable \
  -Dprofile=integration \
  -DprojectId="lofty-root-378503" \
  -DdatasetName="test_dataset_name_integration" \
  -DtableName="test_table_name_integration"
```

</details>


<details>
<summary>Insert rows</summary>

A CLI variation of row insertion may be implemented in the future.

To test row insertion, run the following command:

```shell
./gradlew [ cleanTest ] test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryAdminClientIntegrationTest.insert \
  -Dprofile=PROFILE_NAME \
  -DprojectId="PROJECT_ID" \
  -DdatasetName="DATASET_NAME" \
  -DtableName="TABLE_NAME"
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate.
- `PROJECT_ID`: the name of the GCP project ID to target.
- `DATASET_NAME`: the name of the BigQuery dataset to target.
- `TABLE_NAME`: the name of the BigQuery table to target.

Example using the `integration` profile:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryAdminClientIntegrationTest.insert \
  -Dprofile=integration \
  -DprojectId="lofty-root-378503" \
  -DdatasetName="test_dataset_name_integration" \
  -DtableName="test_table_name_integration"
```

</details>
