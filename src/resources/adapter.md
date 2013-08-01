## Adapter "<(:display-name pattern)>"<a id="<(:id pattern)>"></a>

ID: `<(:id pattern)>`

<(:description pattern)>

### Input CIT

`<(:input-cit pattern)>`

### Input TQL

![](<(get-in pattern [:input-tql :file-path])>)

### Triggered CI Data

Name | Value
|:-------|:-------|
<(for [s (:triggered-ci-data pattern)]
(format "`%s` | `%s` %s\n" (:name s) (:value s) (if-let [d (seq (:description s))] (str "- " d) ""))
)>

### Used scripts

<(for [s (:used-scripts pattern)] ">
  * `<(str (:name s))>`
<")>

### Discovered CITs
<(for [c (sort (:discovered-classes pattern))] ">
  * <(str c)>
<")>

### Global Configuration Files

<(for [f (:global-configuration-files pattern)] ">
  * `<(str f)>`
<")>

### Parameters

Name | Type | Description
|:-------|:-------|:-------|
<(for [p (:parameters pattern)]
(format "`%s` | `%s` | %s\n" (:name p) (:type p) (:description p))
)>

### Discovery Flow

<(:discovery-flow pattern)>


### Prerequisites

##### Set up credentials
