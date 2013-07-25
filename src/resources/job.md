## Job "<(:display-name job)>"<a id="<(:id job)>"></a>

<(:description job)>

### Adapter

ID: [`<(:pattern-id job)>`](#<(:pattern-id job)>)

### Trigger TQL

<(:trigger-tql job)>

### Parameters

<(for [p (:parameters job)] ">
  * <(format "`%s` [`%s`] - %s" (:name p) (:type p) (:description p))>
<")>
