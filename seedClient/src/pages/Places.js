import React from 'react';
import { serverURL } from '../config.json'
export default class Places extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            places: [],
            initialPLaces: []
        }
    }
    componentDidMount() {
        fetch(serverURL + "api/places")
            .then(res => {
                return res.json();
            })
            .then(places => {
                this.setState({ places, initialPLaces: places })
                console.log("places", places)
            })
    }
    
    filterList = (event) => {
        var updatedList = this.state.initialPLaces;
        var value = event.target.value.toLowerCase();
        updatedList = updatedList.filter(function(place)  {
            return place.city.toLowerCase().search(value) !== -1 ||
                   place.description.toLowerCase().search(value) !== -1           
        });
        this.setState({places: updatedList});
    }


    render() {
        const places = this.state.places;
        
        return (
            <div className="container">
                <h2>Places</h2>
                <div>
                    <input
                        type="text"
                        placeholder="Search"
                        onChange={this.filterList}
                    />
                </div>
                {console.log("query", this.state.query)}
                <div id="list" className="row">
                    {
                        places.map((place) => {
                            return (
                                <div key={place.id} className="col-sm-2" style={{ width: 254 }}>
                                    <img src={place.imageUrl} style={{ width: 250 }} />
                                    <div >
                                        <h4 >{place.city}</h4>
                                        <p>{place.description}</p>
                                        <p>ZIP: {place.zip}</p>
                                        <p>Rating: {place.rating}</p>
                                    </div>
                                </div>
                            )

                        })
                    }
                </div>
            </div>
        )
    }
}

