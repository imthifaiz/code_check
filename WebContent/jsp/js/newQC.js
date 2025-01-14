<!--
function validateQC(form)
{

    if ((form.QTYAC.value.length < 1) || (form.QTYAC.value > form.QTYBAL.value))
    {
    alert("Qty is not valid please check you qty..!");
    form.QTYAC.focus();
    return false;
    }

    if (form.TRKIND.value=="B")
    {
      if (form.BATCH.value.length < 1)
      {
        alert("Please Enter Batch ID..!");
        form.BATCH.focus();
        return false;
      }
    }

    if (form.SLIFEIND.value=="Y")
    {
        alert("slife!");
      if (form.SLED.value.length < 4)
      {
        alert("Please Enter Shelf Expiry Date dd/mm/yyyy format..!");
        form.SLED.focus();
        return false;
      }
    }

    if (form.USERFLG1.value.length < 1)
    {
    alert("Please Select Certificate Of Analysis Status..!");
    form.USERFLG1.focus();
    return false;
    }


}


// -->