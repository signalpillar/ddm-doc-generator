## Job "<(:display-name job)>"<a id="<(:id job)>"></a>

<(:description job)>

### Adapter

ID: [`<(:pattern-id job)>`](#<(:pattern-id job)>)

### Trigger TQL
<(for [tql (:trigger-tqls job)]
(format "![%s](%s)\n\n" (:file-name tql) (:file-path tql))
)>

### Parameters

<(when (seq (:parameters job)) "
Name | Type | Description
|:-------|:-------|:-------|")>
<(for [p (:parameters job)]
(format "`%s` | `%s` | %s\n" (:name p) (:type p) (:description p))
)>
