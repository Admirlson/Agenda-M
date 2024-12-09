$(document).ready(function(){
    showDataPicker();
});

function showDataPicker(){
    $(function(){
        
        $('#date').datepicker({
            format:'dd/mm/yyyy',
           startDate:new Date()
            // endDate:new Date()
        });
        $('#date2').datepicker({
            format:'dd/mm/yyyy',
           startDate:new Date()
            // endDate:new Date()
        });
       

        
    });
}