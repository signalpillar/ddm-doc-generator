## Adapter "{{display-name}}"<a id="{{id}}"></a>

ID: `{{id}}`

{{description}}

### Input CIT

`{{input-cit}}`

### Input TQL

![]({{input-tql.file-path}})

### Triggered CI Data

| Name | Value | Description |
|:-------|:-------|:-------|{% for t in triggered-ci-data %}
| `{{t.name}}` | `{{t.value}}` | {{t.description}} |{% endfor %}

### Used scripts

{% for s in used-scripts %}
  * `{{s.name}}`
{% endfor %}


### Discovered CITs
{% for c in discovered-classes %}
  * {{c}}
{% endfor %}


### Global Configuration Files

{% for f in global-configuration-files %}
  * `{{f}}`
{% endfor %}

### Parameters

{% if has-parameters %}
| Name | Type | Description |
|:-------|:-------|:-------|{% for p in parameters %}
| `{{p.name}}` | `{{p.type}}` | {{p.description}} |{% endfor %}
{% endif %}

### Discovery Flow

{{discovery-flow}}


### Prerequisites

##### Set up credentials

{% for p in protocols %}
  * `{{p}}`
{% endfor %}
