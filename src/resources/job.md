## Job "<(:display-name job)>"<a id="<(:id job)>"></a>

<(:description job)>

### Adapter

ID: [`<(:pattern-id job)>`](#<(:pattern-id job)>)

### Trigger TQL

![](<(get-in job [:trigger-tql :file-path])>)

### Parameters

<(when (seq (:parameters job)) "
Name | Type | Description
|:-------|:-------|:-------|")>
<(for [p (:parameters job)]
(format "`%s` | `%s` | %s\n" (:name p) (:type p) (:description p))
)>
