package ilo.ingredienttd5springstd24057.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockValue {
    private Double quantity;
    private UnitEnum unit;
}
