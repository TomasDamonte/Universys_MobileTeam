package universis.universys;

public interface IRequestListener
{
    /**
     * Called when a request finished and the answer from the server arrived.
     * @param request The request that finished its operation.
     * @param response The response got from the server.
     * @return true if other listeners should not be notified, false if they have to.
     */
    public boolean onResponse(CHTTPRequest request, String response);
}
