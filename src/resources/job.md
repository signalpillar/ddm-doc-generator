## Job "{{display-name}}"<a id="{{id}}"></a>

{{description}}

### Adapter

ID: [`{{pattern-id}}`](#{{pattern-id}})

### Trigger TQL
{% for tql in trigger-tqls %}
#### {{tql.file-name}}
![]({{tql.file-path}})
{% endfor %}


### Parameters

{% if has-parameters %}
| Name | Type | Description |
|:-------|:-------|:-------|{% for p in parameters %}
| `{{p.name}}` | `{{p.type}}` | {{p.description}} |{% endfor %}
{% endif %}
