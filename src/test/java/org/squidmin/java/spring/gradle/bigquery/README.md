## Tests

### REST controller

<details>
<summary>Run the "findById" endpoint test</summary>

```shell
./gradlew [ cleanTest ] test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.controller.ControllerTest.lookUpById_givenClientRequest_whenCalled_thenReturn200 \
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
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientIntegrationTest.echoDefaultBigQueryResourceMetadata \
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
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientIntegrationTest.echoDefaultBigQueryResourceMetadata \
  -Dprofile=integration \
  -DprojectId=lofty-root-378503
```

</details>


<details>
<summary>List datasets</summary>

```shell
./gradlew [ cleanTest ] test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientIntegrationTest.listDatasets \
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
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientIntegrationTest.listDatasets \
  -Dprofile=integration \
  -DprojectId="lofty-root-378503"
```

</details>


<details>
<summary>Check whether a dataset exists</summary>

```shell
./gradlew [ cleanTest ] test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientIntegrationTest.datasetExists \
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
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientIntegrationTest.datasetExists \
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
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientIntegrationTest.createDataset \
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
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientIntegrationTest.createDataset \
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
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientIntegrationTest.deleteDataset \
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
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientIntegrationTest.deleteDataset \
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
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientIntegrationTest.deleteDatasetAndContents \
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
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientIntegrationTest.deleteDatasetAndContents \
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
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientIntegrationTest.createTableWithDefaultSchema \
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
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientIntegrationTest.createTableWithDefaultSchema \
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
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientIntegrationTest.createTableWithCustomSchema \
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
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientIntegrationTest.createTableWithCustomSchema \
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
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientIntegrationTest.deleteTable \
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
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientIntegrationTest.deleteTable \
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
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientIntegrationTest.insert \
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
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientIntegrationTest.insert \
  -Dprofile=integration \
  -DprojectId="lofty-root-378503" \
  -DdatasetName="test_dataset_name_integration" \
  -DtableName="test_table_name_integration"
```

</details>
