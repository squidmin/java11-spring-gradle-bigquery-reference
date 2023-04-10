# spring-rest-labs


### Quickstart

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


### REST Controller methods

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
<summary>Create a table with the configured default schema</summary>

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
<summary>Create a table with a custom schema</summary>

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

Example using the `integration` profile:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryAdminClientIntegrationTest.createTableWithCustomSchema \
  -Dprofile=integration \
  -DprojectId="lofty-root-378503" \
  -DdatasetName="test_dataset_name_integration" \
  -DtableName="test_table_name_integration" \
  -Dschema="id,string;fieldA,string;fieldB,string"
```

</details>


<details>
<summary>Delete table</summary>

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
