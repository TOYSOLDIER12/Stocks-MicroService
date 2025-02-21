package ma.xproce.stocksmicroservice.dao.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyPrice {

    private String ticker;
    private long timestamp;
    private float last;
    private float prevClose;
    private float open;
    private float high;
    private float low;
    private float mid;
    private float volume;
    private float bidSize;
    private float bidPrice;
    private float askSize;
    private float askPrice;
}
