<!--
function validateItem(form)
{
    alert("Please Enter Item Number !");
    if (form.ITEM.value.length < 1)
    {
    alert("Please Enter Item Number !");
    form.ITEM.focus();
    return false;
    }
    if (form.ITEMTYPE.value.length < 1)
    {
    alert("Please Select Item Type !");
    form.ITEMTYPE.focus();
    return false;
    }

    if (form.TRKIND.value.length < 1)
    {
    alert("Please Select Track Indicator !");
    form.TRKIND.focus();
    return false;
    }
    if (form.SLIFEIND.value.length < 1)
    {
    alert("Please Select Shelf Life Indicator !");
    form.SLIFEIND.focus();
    return false;
    }

    if (form.SLIFEIND.value=="Y")
    {
      if (form.SLIFE.value <= 0)
      {
        alert("Please Enter Shelf Expiry Months..!");
        form.SLIFE.focus();
        return false;
      }
    }

   if (form.QCIND.value=="Y")
    {
      if (form.QUARDAYS.value <= 0)
      {
        alert("Please Enter Quanrantine Days..!");
        form.QUARDAYS.focus();
        return false;
      }
    }


    if (form.QCIND.value.length < 1)
    {
    alert("Please Select QC Indicator !");
    form.QCIND.focus();
    return false;
    }

    if (form.QUARIND.value.length < 1)
    {
    alert("Please Select Quarantine Indicator !");
    form.QUARIND.focus();
    return false;
    }
    if (form.STKUOM.value.length < 1)
    {
    alert("Please Select UOM !");
    form.STKUOM.focus();
    return false;
    }


}
// -->